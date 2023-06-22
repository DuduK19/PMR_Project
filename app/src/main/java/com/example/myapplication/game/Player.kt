package com.example.myapplication.game

class Player(var main:MutableList<String> = mutableListOf(), var value:Int = 0, var cheatScore:Int = 0, var nbChoix:Int = 0, val id:Int) {

    fun setValue() {
        value = calculateValue()
    }

    private fun calculateValue(): Int {
        var totalValue = 0
        for (card in main) {
            if (card =="A"){
                if (totalValue + 11<=21) totalValue+=11
                }
            else{
                val cardValue = card.toInt()
                totalValue += cardValue
            }
        }
        return totalValue
    }

    fun addCard(card:String){
        main.add(card)
    }

    fun setCheatScore(decision:String, i:Int){
        setValue()
        when (decision) {
            "je prends" -> {
                nbChoix += 1
                when{
                    (i>=5 && value>14) -> cheatScore = (cheatScore * (nbChoix - 1) + 1) / nbChoix
                    (i<5 && value>14) -> cheatScore = (cheatScore * (nbChoix - 1)) / nbChoix
                }
            }
            "je passe" -> {
                nbChoix += 1
                when{
                    (i>=-5 && value<=14) -> cheatScore = (cheatScore * (nbChoix - 1)) / nbChoix
                    (i<=-5 && value<=14) -> cheatScore = (cheatScore * (nbChoix - 1) + 1) / nbChoix
                }
            }
        }
    }

    override fun toString(): String {
        return "Player $id /Main: ${main.toString()}/Valeur de la main: $value " +
                "nbChoix: $nbChoix/ cheatScore:$cheatScore"
    }
}