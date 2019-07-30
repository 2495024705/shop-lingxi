<template>
    <router-view></router-view>
</template>

<script>
export default {
  name:"App",
   provide(){
      reload: this.reload
    }
}
</script>
