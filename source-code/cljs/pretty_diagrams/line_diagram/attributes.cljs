
(ns pretty-diagrams.line-diagram.attributes
    (:require [fruits.css.api                     :as css]
              [pretty-css.api                     :as pretty-css]
              [pretty-diagrams.line-diagram.utils :as line-diagram.utils]
              [metamorphic-content.api :as metamorphic-content]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-section-attributes
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; @param (map) section-props
  ; {:color (keyword or string)
  ;  :label (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)
  ;   {:width (string)}}
  [_ diagram-props {:keys [color label] :as section-props}]
  (let [value-ratio (line-diagram.utils/section-props->value-ratio diagram-props section-props)]
       (-> {:class :pd-line-diagram--section
            :style {:width (css/percent value-ratio)}}
           (pretty-css/badge-attributes {:badge-content label :badge-position :bl})
           (pretty-css/color-attributes {:fill-color color}))))

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
      (pretty-css/indent-attributes       diagram-props)
      (pretty-css/element-size-attributes diagram-props)))

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
      (pretty-css/class-attributes        diagram-props)
      (pretty-css/state-attributes        diagram-props)
      (pretty-css/outdent-attributes      diagram-props)
      (pretty-css/wrapper-size-attributes diagram-props)))
