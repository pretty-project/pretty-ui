
(ns pretty-diagrams.line-diagram.attributes
    (:require [fruits.css.api                     :as css]
              [metamorphic-content.api            :as metamorphic-content]
              [pretty-build-kit.api               :as pretty-build-kit]
              [pretty-diagrams.line-diagram.utils :as line-diagram.utils]))

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
           (pretty-build-kit/badge-attributes {:badge-content label :badge-position :bl})
           (pretty-build-kit/color-attributes {:fill-color color}))))

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
      (pretty-build-kit/indent-attributes       diagram-props)
      (pretty-build-kit/element-size-attributes diagram-props)
      (pretty-build-kit/style-attributes        diagram-props)))

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
      (pretty-build-kit/class-attributes        diagram-props)
      (pretty-build-kit/outdent-attributes      diagram-props)
      (pretty-build-kit/state-attributes        diagram-props)
      (pretty-build-kit/wrapper-size-attributes diagram-props)))
