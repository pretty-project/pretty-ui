
(ns elements.line-diagram.attributes
    (:require [css.api                       :as css]
              [elements.line-diagram.helpers :as line-diagram.helpers]
              [pretty-css.api                :as pretty-css]))


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-section-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-background-color (keyword)}
  ;  :style (map)
  ;   {:width (string)}}
  [_ diagram-props {:keys [color] :as section-props}]
  (let [value-ratio (line-diagram.helpers/section-props->value-ratio diagram-props section-props)]
       {:class :e-line-diagram--section
        :data-fill-color color
        :style {:width (css/percent value-ratio)}}))

(defn diagram-sections-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:strength (px)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)
  ;   {:height (string)}}
  [_ {:keys [strength]}]
  {:class :e-line-diagram--sections
   :style {:height (css/px strength)}})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-body-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as diagram-props}]
  (-> {:class :e-line-diagram--body
       :style style}
      (pretty-css/indent-attributes diagram-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ diagram-props]
  (-> {:class :e-line-diagram}
      (pretty-css/default-attributes diagram-props)
      (pretty-css/outdent-attributes diagram-props)))
