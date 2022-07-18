package com.stubuy.catalog.dbservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stubuy.catalog.dto.request.BranchRegisterRequest;
import com.stubuy.catalog.dto.response.BranchResponse;
import com.stubuy.catalog.dto.response.CourseResponse;
import com.stubuy.catalog.dto.response.GetAllBranchResponse;
import com.stubuy.catalog.entity.BranchEntity;
import com.stubuy.catalog.entity.CourseEntity;
import com.stubuy.catalog.repository.BranchRepository;
import com.stubuy.catalog.utils.EntityToResponseConverter;

@Component
public class BranchDatabaseServiceImp implements BranchDatabaseService {

  @Autowired
  private CourseDatabaseService courseDatabaseService;

  @Autowired
  private BranchRepository branchRepository;

  @Override
  public BranchResponse saveBranchInfo(BranchRegisterRequest branchRegisterRequest) {
    BranchEntity branchEntity = new BranchEntity();
    try {
      branchEntity.setBranch_Name(branchRegisterRequest.getBranchName());
      CourseResponse courseResponse =
          courseDatabaseService.getCourseByID(branchRegisterRequest.getCourseId());
      CourseEntity course = CourseEntity.builder().courseId(courseResponse.getCourseId())
          .courseName(courseResponse.getCourseName()).build();
      branchEntity.setCourseEntity(course);
      branchEntity = branchRepository.save(branchEntity);
    } catch (Exception exception) {
      throw new RuntimeException();
    }
    return EntityToResponseConverter.branchEntityToBranchResponse(branchEntity);
  }

  @Override
  public List<GetAllBranchResponse> getAllBranch() {
    List<GetAllBranchResponse> responses = new ArrayList<>();
    try {
      List<BranchEntity> branchEntities = branchRepository.findAll();
      for (BranchEntity branchEntity : branchEntities) {
        GetAllBranchResponse branchResponse =
            GetAllBranchResponse.builder().branch_id(branchEntity.getBranchId())
                .branch_Name(branchEntity.getBranch_Name()).build();
        responses.add(branchResponse);
      }
    } catch (Exception exception) {
      throw new RuntimeException();
    }
    return responses;
  }
}
