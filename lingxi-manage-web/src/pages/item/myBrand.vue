<template>
    <div>
       <v-layout class="px-2 pb-4">
        <v-flex xs2><v-btn color="info">新增品牌</v-btn></v-flex>
        <v-spacer> </v-spacer>
        <v-flex xs2><v-text-field label="搜索" hide-details append-icon="search" v-model="key"></v-text-field></v-flex>
        </v-layout>
          
     <v-data-table
      :headers="headers"
      :items="brands"
      :pagination.sync="pagination"
      :total-items="totalBrands"
      :loading="loading"
      class="elevation-1"
    >
      <template slot="items" slot-scope="props">
        <td class="text-xs-center">{{ props.item.id }}</td>
        <td class="text-xs-center">{{ props.item.name }}</td>
       <td class="text-xs-center"><img v-if="props.item.image" :src="props.item.image" width="130" height="40"/></td>
        <td class="text-xs-center">{{ props.item.letter }}</td>
        <td class="text-xs-center">
            <v-btn flat icon color="info"><v-icon>thumb_up</v-icon></v-btn>
              <v-btn flat icon color="error"><v-icon>thumb_up</v-icon></v-btn></td>
      </template>
    </v-data-table>
    </div>
</template>
<script>
export default {
    name: "myBrand",
    data () {
      return {
        headers: [
       {text: "品牌ID", value:'id',align: 'center', sortable: true},
       {text: "品牌名称", value: 'name',align: 'center', sortable: false},
       {text: "品牌LOGO", value: 'image',align: 'center', sortable: false},
       {text: "品牌首字母", value: 'letter',align: 'center', sortable: true},
         {text: "修改 删除", align: 'center'}
        ],
        brands:[],
        pagination:{},
        totalBrands:0,
        loading: false,
      }
},
created(){
 this.$http.get('/brand/page', {
    params: {
      page: 1
    }
  })
  .then(function (response) {
    console.log(response);
  })
  .catch(function (error) {
    console.log(error);
  });
          this.totalBrands=15;
          this.loadBrands();
},
watch:{
key(){
  this.pagination.page=1;
  
},
pagination:{
  deep:true,
  handler(){
    this.loadBrands();
  }
}
},
methods:{
loadBrands(){
  this.loading=true
  this.$http.get('/item/brand/page',{
    params:{
      page:this.pagination.page,//当前页
      rows:this.pagination.rows,//每页字段
      sortBy:this.pagination.sortBy,
      desc:this.pagination.desc,
      key:this.key
    }
  }).then(response=>{
    this.brands=response.data.items;
    this.totalBrands=response.data.totalBrands
  })
  this.loading=false
}
}
}

</script>
<style scoped>
</style>



