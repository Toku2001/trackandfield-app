package io.github.Toku2001.trackandfieldapp.repository;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import io.github.Toku2001.trackandfieldapp.entity.Training_Info;

@Mapper
public interface TrainingMapper {
    //練習日誌登録
	int registerTraining(
		@Param("userId") long userId,
		@Param("trainingTime") LocalDate trainingTime,
		@Param("trainingPlace") String trainingPlace,
		@Param("trainingComments") String trainingComments
		);
	
	 //練習日誌を日付で取得
	 Training_Info getTrainingByDate(
			@Param("userId") long userId,
			@Param("trainingTime") LocalDate trainingTime);
	 
	//  //練習日誌をユーザーIDで取得
	//  Training_Info getTrainingByUserId(
	// 		@Param("userId") long userId);
 
    //練習日誌編集
	 int changeTraining(
		@Param("userId") long userId,
		@Param("trainingId") int trainingId,
		@Param("trainingTime") LocalDate trainingTime,
		@Param("trainingPlace") String trainingPlace,
		@Param("trainingComments") String trainingComments
		);
    
     //練習日誌削除
     int deleteTraining(
     		@Param("userId") long userId,
     		@Param("trainingTime") LocalDate trainingTime);
    
    // //全ユーザー情報を取得
    // List <Training_Info> getTrainings();
}

