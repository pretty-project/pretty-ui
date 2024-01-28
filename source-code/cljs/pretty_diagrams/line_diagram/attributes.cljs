
(ns pretty-diagrams.line-diagram.attributes
    (:require [fruits.css.api                     :as css]
              [metamorphic-content.api            :as metamorphic-content]
              
              [pretty-diagrams.line-diagram.utils :as line-diagram.utils]
              [pretty-css.accessories.api :as pretty-css.accessories]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api :as pretty-css.basic]
              [pretty-css.layout.api :as pretty-css.layout]))

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
           (pretty-css.accessories/badge-attributes     {:badge-content label :badge-position :bl})
           (pretty-css.appearance/background-attributes {:fill-color color}))))

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
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ diagram-props]
  (-> {:class :pd-line-diagram--body}
      (pretty-css.layout/indent-attributes       diagram-props)
      (pretty-css.layout/element-size-attributes diagram-props)
      (pretty-css.basic/style-attributes        diagram-props)))

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
      (pretty-css.basic/class-attributes        diagram-props)
      (pretty-css.layout/outdent-attributes      diagram-props)
      (pretty-css.basic/state-attributes        diagram-props)
      (pretty-css.appearance/theme-attributes        diagram-props)
      (pretty-css.layout/wrapper-size-attributes diagram-props)))
