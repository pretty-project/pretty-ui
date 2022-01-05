
(ns app-extensions.trader.styles
    (:require [mid-fruits.candy :refer [param]]
              [mid-fruits.css   :as css]
              [mid-fruits.math  :as math]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn monitor-style-rules
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (str ".trader--price-box { opacity: 0 }"
       ".trader--monitor-chart.r-mounted, .trader--monitor-settings.r-mounted { opacity: 1 }"
       ".trader--monitor-chart,           .trader--monitor-settings           { opacity: 0; transition: opacity .1s }"
       ".trader--monitor-chart:hover .trader--price-box { opacity: 1 }"
       "[data-subscribed=\"true\"]  .trader--sensor:hover { background-color: var( --soft-purple ) }"
       "[data-subscribed=\"false\"] .trader--sensor:hover { background-color: var( --color-highlight ) }"))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn overlay-center-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:align-items     "center"
   :display         "flex"
   :flex-direction  "column"
   :height          "100%"
   :justify-content "center"
   :padding         "0 24px"
   :width           "100%"})

(defn column-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ([]
   (column-style {}))

  ([additional-style]
   (merge {:display        "flex"
           :flex-direction "column"
           :flex-wrap      "wrap"}
          (param additional-style))))

(defn row-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ([]
   (row-style {}))

  ([additional-style]
   (merge {:display        "flex"
           :flex-direction "row"
           :flex-wrap      "wrap"}
          (param additional-style))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-list-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:display   "flex"
   :flex-wrap "wrap"})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-structure-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:height "468px"
   :padding "48px 24px 0 24px"
   :width  "50vw"})

(defn box-body-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:background-color "var( --fill-color )"
   :border-radius    "var( --border-radius-xxl )"
   :height           "420px"
   :overflow         "hidden"
   :width            "100%"})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn monitor-connection-toggle-button-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [subscribed?]}]
  {:border-color  (css/var (if subscribed? "border-color-primary" "border-color-highlight"))
   :border-radius "24px"
   :border-style  "solid"
   :border-width  "2px"})

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

(defn monitor-bars-overlay
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:height "calc(100% - 84px)"
   :left "0"
   :position "absolute"
   :top "60px"
   :width "100%"})

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
  [_ {:keys [monitor-watching?]}]
  {:fill         "none"
   :stroke       (if monitor-watching? "var( --color-secondary )"
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
  [_ {:keys [kline-list]} dex]
  (let [count        (count kline-list)
        sensor-left  (math/percent count dex)
        sensor-width (math/percent count 1)]
       {:cursor   "pointer"
        :height   "100%"
        :opacity  ".5"
        :position "absolute"
        :top      "0"
        :left  (css/percent sensor-left)
        :width (css/percent sensor-width)}))

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

(defn box-br-controls-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:bottom   "12px"
   :display  "flex"
   :position "absolute"
   :right    "12px"})

(defn box-bl-controls-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:bottom   "12px"
   :display  "flex"
   :left     "12px"
   :position "absolute"})

(defn box-tl-controls-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:display  "flex"
   :left     "12px"
   :position "absolute"
   :top      "12px"})

(defn box-tr-controls-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:display  "flex"
   :position "absolute"
   :right    "12px"
   :top      "12px"})

(defn box-tc-controls-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:display  "flex"
   :justify-content "center"
   :left    "0"
   :position "absolute"
   :top      "12px"
   :width    "100%"})


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

(defn price-box-range-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:align-items     "center"
   :display         "flex"
   :font-weight     "600"
   :justify-content "center"
   :width           "80px"})

(defn price-box-volume-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:align-items     "center"
   :display         "flex"
   :font-weight     "600"
   :justify-content "center"
   :opacity         ".3"
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
   :min-width   "60px"
   :text-align  "center"})

(defn price-box-prices-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:font-size   "13px"
   :font-weight "500"
   :line-height "21px"
   :text-align  "center"
   :width       "120px"})

(defn price-box-labels-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:font-size   "13px"
   :font-weight "500"
   :line-height "21px"
   :opacity     "0.3"
   :text-align  "right"
   :width       "60px"})

(defn price-box-low-high-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:font-size   "13px"
   :font-weight "500"
   :line-height "21px"
   :opacity     "0.3"
   :text-align  "center"
   :width       "120px"})



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
   :font-size        "13px"
   :font-weight      "500"
   :height           "372px"
   :line-height      "16px"
   :overflow-y       "auto"
   :padding          "12px 0 12px 12px"
   :width            "100%"})

(defn log-item-module-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:display     "inline-block"
   :font-weight "600"
   :opacity     ".65"
   :width       "150px"})

(defn log-item-timestamp-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [highlighted? warning?]}]
  {:display     "inline-block"
   :flex-shrink "0"
   :font-family "monospace"
   :font-weight "600"
   :opacity     ".65"
   :width       "80px"
   :color (cond highlighted? "rgb( 80, 230, 255)"
                warning?     "rgb(255,  80, 230)"
                :default     "rgb(255, 230,  80)")})

(defn log-item-message-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [highlighted? warning?]}]
  {:white-space "normal"
   :font-family "monospace"
   :color (cond highlighted? "rgb(80,  230, 255)"
                warning?     "rgb(255,  80, 230)"
                :default     "rgb(255, 230,  80)")})



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

(defn editor-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:height  "calc(100vh - 48px)"
   :padding "48px"
   :width   "100%"})

(defn editor-structure-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:background-color "rgb( 60,  60,  60)"
   :border-radius    "var( --border-radius-xxl )"
   :display          "flex"
   :flex-direction   "column"
   :font-family      "monospace"
   :height           "100%"
   :overflow         "hidden"
   :width            "100%"})

(defn editor-textarea-wrapper-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  ; A textarea elemet, az esetlegesen megjelenő scroll-bar elhelyezkedése
  ; miatt szükséges egy wrapper elemben megjeleníteni!
  {:flex-grow 1})

(defn editor-textarea-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:color       "rgb(255, 230,  80)"
   :font-size   "13px"
   :font-weight "500"
   :height      "calc(100% - 48px)"
   :margin      "24px"
   :resize      "none"
   :width       "calc(100% - 48px)"})

(defn editor-log-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [log-visible?]}]
  {:bottom   "0"
   :position "absolute"
   :right    "0"
   :transform (if log-visible? "translateY(0)"
                               "translateY(144px)")})

(defn editor-log-menu-bar-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:display  "flex"
   :position "absolute"
   :right    "0"
   :top      "-48px"})

(defn editor-log-body-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:background-color "rgb(40, 40, 40)"
   :border-radius    "24px 0 0 0"
   :font-size        "13px"
   :font-weight      "500"
   :height           "144px"
   :line-height      "16px"
   :opacity          ".6"
   :overflow-y       "auto"
   :padding          "12px 0 12px 12px"
   :width            "720px"})

(defn editor-menu-bar-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:position "absolute"
   :right    "0"
   :top      "24px"})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn synchronizing-label-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:align-items      "center"
   :background-color "var( --fill-color )"
   :display          "flex"
   :flex-direction   "column"
   :height           "100%"
   :justify-content  "center"
   :left             "0"
   :position         "absolute"
   :top              "0"
   :width            "100%"})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn source-code-preview-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:background-color "rgb(60, 60, 60)"
   :border-radius    "12px"
   :color            "rgb(255, 230, 80)"
   :display          "flex"
   :font-family      "monospace"
   :font-size        "10px"
   :height           "200px"
   :line-height      "12px"
   :overflow         "hidden"
   :padding          "6px"
   :width            "356px"})

(defn source-code-preview-icon-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:align-items     "center"
   :color           "white"
   :display         "flex"
   :flex-direction  "column"
   :height          "100%"
   :justify-content "center"
   :left            "0"
   :position        "absolute"
   :top             "0"
   :width           "100%"})
