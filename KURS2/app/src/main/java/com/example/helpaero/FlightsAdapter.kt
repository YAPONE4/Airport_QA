package com.example.helpaero

import androidx.recyclerview.widget.RecyclerView
import com.example.helpaero.data.Flight

class FlightsAdapter(private val flights: List<Flight>) :
    RecyclerView.Adapter<FlightsAdapter.FlightViewHolder>() {

    class FlightViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val number: android.widget.TextView = itemView.findViewById(R.id.tvFlightNumber)
        val time: android.widget.TextView = itemView.findViewById(R.id.tvFlightTime)
        val destination: android.widget.TextView = itemView.findViewById(R.id.tvFlightDestination)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): FlightViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_flight, parent, false)
        return FlightViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        val flight = flights[position]
        holder.number.text = flight.number
        holder.time.text = flight.time
        holder.destination.text = flight.destination
    }

    override fun getItemCount(): Int = flights.size
}