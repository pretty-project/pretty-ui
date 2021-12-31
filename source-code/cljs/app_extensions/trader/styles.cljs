
(ns app-extensions.trader.styles
    (:require [mid-fruits.candy :refer [param]]
              [mid-fruits.css   :as css]
              [mid-fruits.math  :as math]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn monitors-style-rules
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (str ".trader--price-box { opacity: 0 }"
       ".trader--monitor-chart.r-mounted, .trader--monitor-settings.r-mounted { opacity: 1 }"
       ".trader--monitor-chart,           .trader--monitor-settings           { opacity: 0; transition: opacity .1s }"
       ".trader--monitor-chart:hover .trader--price-box { opacity: 1 }"
       "[data-watching=\"true\"]  .trader--sensor:hover { background-color: var( --soft-purple ) }"
       "[data-watching=\"false\"] .trader--sensor:hover { background-color: var( --color-highlight ) }"))

(defn log-style-rules
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (str ".trader--log-item:hover { background-color: red !important }"))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-list-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:display   "flex"
   :flex-wrap "wrap"})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn monitor-structure-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:height "468px"
   :padding "0 24px"
   :width  "50vw"})

(defn monitor-body-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:background-color "var( --fill-color )"
   :border-radius    "var( --border-radius-xxl )"
   :height           "420px"
   :overflow         "hidden"
   :width            "100%"})

(defn monitor-settings-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:align-items     "center"
   :display         "flex"
   :flex-direction  "column"
   :height          "100%"
   :justify-content "center"
   :width           "100%"})

(defn monitor-details-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:align-items     "center"
   :display         "flex"
   :flex-direction  "column"
   :height          "100%"
   :justify-content "center"
   :left            "0"
   :position        "absolute"
   :top             "0"
   :width           "100%"})

(defn monitor-chart-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:height  "100%"
   :padding "60px 0 24px 0"
   :width   "100%"})

(defn monitor-chart-svg-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:height "100%"
   :width  "100%"})

(defn monitor-chart-line-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [watching?]}]
  {:fill         "none"
   :stroke       (if watching? "var( --color-secondary )"
                               "var( --color-highlight )")
   :stroke-width ".15px"})

(defn monitor-chart-price-data-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:bottom          "0"
   :color           "var( --color-muted )"
   :display         "flex"
   :font-size       "12px"
   :justify-content "center"
   :left            "0"
   :position        "absolute"
   :width           "100%"})

(defn monitor-chart-price-label-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:padding "0 0 0 12px"})

(defn monitor-chart-price-value-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:font-weight "600"
   :padding     "0 12px 0 6px"})

(defn price-sensors-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:display  "flex"
   :height   "100%"
   :left     "0"
   :position "absolute"
   :top      "0"
   :width    "100%"})

(defn price-sensor-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [kline-list]}]
  (let [sensor-width (math/percent (count kline-list) 1)]
       {:cursor  "pointer"
        :height  "100%"
        :opacity ".5"
        :width   (css/percent sensor-width)}))

(defn monitor-chart-y-labels-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:color           "var( --color-muted )"
   :display         "flex"
   :font-size       "12px"
   :font-weight     "600"
   :justify-content "space-between"
   :line-height     "32px"
   :height          "48px"
   :padding         "0 16px"
   :width           "100%"})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn monitor-br-controls-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:bottom   "12px"
   :display  "flex"
   :position "absolute"
   :right    "12px"})

(defn monitor-bl-controls-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:bottom   "12px"
   :left     "12px"
   :position "absolute"})

(defn monitor-tl-controls-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:left     "12px"
   :position "absolute"
   :top      "12px"})

(defn monitor-controls-timer-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:left     "0"
   :position "absolute"
   :top      "0"})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn server-status-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:color           "var( --color-highlight )"
   :display         "flex"
   :font-size       "12px"
   :justify-content "center"
   :left            "0"
   :line-height     "48px"
   :position        "absolute"
   :top             "12px"
   :width           "100%"})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn price-box-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:display  "flex"
   :position "absolute"
   :right    "12px"
   :top      "12px"})

(defn price-box-diff-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:align-items     "center"
   :display         "flex"
   :font-weight     "600"
   :justify-content "center"
   :width           "80px"})

(defn price-box-pos-diff-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:color      "var( --color-success )"
   :text-align "center"
   :width      "90px"})

(defn price-box-neg-diff-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:color      "var( --color-warning )"
   :text-align "center"
   :width      "90px"})

(defn price-box-timestamps-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:color       "var( --color-muted )"
   :font-size   "13px"
   :font-weight "500"
   :line-height "21px"
   ; min-width: egyes esetekben dátummal együtt jelenik meg az időbélyegző
   :min-width   "80px"
   :text-align  "center"})

(defn price-box-prices-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:font-size   "13px"
   :font-weight "500"
   :line-height "21px"
   :text-align  "center"
   :width       "120px"})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn controls-structure-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:height  "468px"
   :padding "0 24px"
   :width   "50vw"})

(defn controls-body-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:background-color "var( --fill-color )"
   :border-radius    "var( --border-radius-xxl )"
   :display          "flex"
   :flex-direction   "column"
   :height           "420px"
   :overflow         "hidden"
   :width            "100%"})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn log-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:flex-grow "1"})

(defn log-body-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:background-color "rgb( 60,  60,  60)"
   :color            "rgb(255, 230,  80)"
   :font-size        "13px"
   :font-weight      "500"
   :height           "372px"
   :line-height      "16px"
   :overflow-y       "auto"
   :padding          "12px 0 0 12px"})

(defn log-item-module-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:display     "inline-block"
   :font-weight "600"
   :opacity     ".65"
   :width       "150px"})

(defn log-item-timestamp-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:display     "inline-block"
   :font-weight "600"
   :opacity     ".65"
   :width       "80px"})

(defn log-item-message-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn settings-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:display        "flex"
   :flex-direction "column"
   :padding        "12px 24px"})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn account-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:display        "flex"
   :flex-direction "column"
   :padding        "12px 24px"})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn listener-list-structure-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:height "468px"
   :padding "0 24px"
   :width  "50vw"})

(defn listener-list-body-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:background-color "var( --fill-color )"
   :border-radius    "var( --border-radius-xxl )"
   :height           "420px"
   :overflow         "hidden"
   :width            "100%"})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn overlay-center
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:align-items     "center"
   :display         "flex"
   :flex-direction  "column"
   :height          "100%"
   :justify-content "center"
   :width           "100%"})

(defn row
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ([]
   (row {}))

  ([x]
   (merge {:display        "flex"
           :flex-direction "row"}
          (param x))))
