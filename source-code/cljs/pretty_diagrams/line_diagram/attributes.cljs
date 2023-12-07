
(ns pretty-diagrams.line-diagram.attributes
    (:require [fruits.css.api                     :as css]
              [pretty-css.api                     :as pretty-css]
              [pretty-diagrams.line-diagram.utils :as line-diagram.utils]))

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
  (let [value-ratio (line-diagram.utils/section-props->value-ratio diagram-props section-props)]
       {:class           :pd-line-diagram--section
        :data-fill-color color
        :style           {:width (css/percent value-ratio)}}))

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
  {:class :pd-line-diagram--sections
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
  (-> {:class :pd-line-diagram--body
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
  (-> {:class :pd-line-diagram}
      (pretty-css/default-attributes      diagram-props)
      (pretty-css/outdent-attributes      diagram-props)
      (pretty-css/element-size-attributes diagram-props)))
