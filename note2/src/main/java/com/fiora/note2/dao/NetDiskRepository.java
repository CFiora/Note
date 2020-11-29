package com.fiora.note2.dao;

import com.fiora.note2.model.NetDisk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NetDiskRepository extends JpaRepository<NetDisk, Long> {

    @Query(nativeQuery = true, value="select * from net_disk where directory = 0 " +
            "and (name like CONCAT('%',:filter,'%') or path like CONCAT('%',:filter,'%')) " +
            "order by path")
    public List<NetDisk> findByNameOrPath(@Param("filter") String filter);
}
 