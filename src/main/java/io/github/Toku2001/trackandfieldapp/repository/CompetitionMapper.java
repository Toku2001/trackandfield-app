package io.github.Toku2001.trackandfieldapp.repository;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import io.github.Toku2001.trackandfieldapp.entity.Competition_Info;

@Mapper
public interface CompetitionMapper {

    // 大会情報の登録
    int registerCompetition(
        @Param("userId") long userId,
        @Param("competitionName") String competitionName,
        @Param("competitionPlace") String competitionPlace,
        @Param("competitionTime") LocalDate competitionTime,
        @Param("competitionComments") String competitionComments
    );

    // 日付で大会情報を取得
    Competition_Info getCompetitionByDate(
        @Param("userId") long userId,
        @Param("competitionTime") LocalDate competitionTime
    );

    // ユーザーIDで大会情報をすべて取得
    // List<Competition_Info> getCompetitionsByUserId(
    //     @Param("userId") long userId
    // );
    
    // 近日開催の競技会情報を取得
    Competition_Info getNextCompetition(@Param("userId") long userId);

    // 大会情報の更新（日時とユーザーIDで対象を特定）
    int changeCompetition(
        @Param("userId") long userId,
        @Param("competitionId") int competitionId,
        @Param("competitionName") String competitionName,
        @Param("competitionPlace") String competitionPlace,
        @Param("competitionTime") LocalDate competitionTime,
        @Param("competitionComments") String competitionComments
    );

    // 大会情報の削除
    int deleteCompetition(
        @Param("userId") long userId,
        @Param("competitionTime") LocalDate competitionTime
    );

    // 全ての大会情報を取得（管理者向け想定）
    // List<Competition_Info> getAllCompetitions();
}