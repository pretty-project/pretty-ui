
(ns full-calendar.views
    (:require ["@fullcalendar/react"           :default FullCalendar]
              ["@fullcalendar/daygrid"         :default dayGridPlugin]
              ["@fullcalendar/timegrid"        :default timeGridPlugin]
              ["@fullcalendar/list"            :default listPlugin]
              ["@fullcalendar/interaction"     :default interactionPlugin]
              ["@fullcalendar/google-calendar" :default googleCalendarPlugin]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn plugin
  ; @usage
  ; [plugin]
  []
  [:div {:class :p-full-calendar}
        [:> FullCalendar
            {:plugins (clj->js [timeGridPlugin dayGridPlugin listPlugin interactionPlugin googleCalendarPlugin])
             :googleCalendarApiKey "xxxxxxxxxxxxxxxxx-xxxxxxxxxxxxxxxxxxxxx"
             :events               {:googleCalendarId "my-user@gmail.com"}
             :initialView  "timeGridWeek"
             :slotDuration "01:00:00"
             :slotMinTime  "05:00:00"
             :slotMaxTime  "22:00:00"}]])
