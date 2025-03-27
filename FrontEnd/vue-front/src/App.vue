<template>
  <div class="container mx-auto p-6">
    <h1 class="text-2xl font-bold mb-4">Operadoras de Saúde</h1>

    <div class="flex mb-4">
      <input
        v-model="query"
        @input="buscarOperadoras(0)"
        type="text"
        placeholder="Digite nome, cidade, UF ou CNPJ..."
        class="border p-2 rounded w-full"
      />
    </div>

    <table class="min-w-full divide-y divide-gray-200">
      <thead class="bg-gray-50">
        <tr>
          <th class="px-4 py-2 text-left">Registro ANS</th>
          <th class="px-4 py-2 text-left">Razão Social</th>
          <th class="px-4 py-2 text-left">CNPJ</th>
          <th class="px-4 py-2 text-left">Cidade</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="operadora in operadoras" :key="operadora.id">
          <td class="px-4 py-2">{{ operadora.regAns }}</td>
          <td class="px-4 py-2">{{ operadora.razaoSocial }}</td>
          <td class="px-4 py-2">{{ operadora.cnpj }}</td>
          <td class="px-4 py-2">{{ operadora.cidade }}</td>
        </tr>
        <tr v-if="operadoras.length === 0">
          <td colspan="4" class="text-center px-4 py-2 text-gray-500">
            Nenhuma operadora encontrada.
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Paginação -->
    <div class="flex justify-center items-center mt-4 space-x-2">
      <button
        :disabled="currentPage === 0"
        @click="buscarOperadoras(currentPage - 1)"
        class="px-4 py-2 bg-gray-200 rounded"
      >
        Anterior
      </button>

      <span>Página {{ currentPage + 1 }} de {{ totalPages }}</span>

      <button
        :disabled="currentPage + 1 >= totalPages"
        @click="buscarOperadoras(currentPage + 1)"
        class="px-4 py-2 bg-gray-200 rounded"
      >
        Próxima
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import axios from 'axios'

const operadoras = ref([])
const query = ref('')
const currentPage = ref(0)
const totalPages = ref(0)
const size = 30

const buscarOperadoras = async (page = 0) => {
  try {
    const response = await axios.get('http://localhost:8080/api/operadoras/find', {
      params: { 
        query: query.value, 
        page, 
        size 
      },
    })

    operadoras.value = response.data.operadoras
    currentPage.value = response.data.currentPage
    totalPages.value = response.data.totalPages
  } catch (error) {
    console.error('Erro ao buscar operadoras:', error)
    operadoras.value = []
  }
}

// Carrega operadoras ao abrir a página
onMounted(() => buscarOperadoras())

// Observa mudanças no query e reseta a página
watch(query, () => buscarOperadoras(0))
</script>

<style scoped>
.container {
  max-width: 900px;
}
</style>
