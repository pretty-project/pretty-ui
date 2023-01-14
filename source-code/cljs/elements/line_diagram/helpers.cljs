
(ns elements.line-diagram.helpers
    (:require [pretty-css.api :as pretty-css]
              [css.api        :as css]
              [math.api       :as math]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-props->total-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) diagram-props
  ; {:sections (maps in vector)}
  ;
  ; @return (integer)
  [{:keys [sections]}]
  (letfn [(f [total-value {:keys [value]}] (+ total-value value))]
         (reduce f 0 sections)))

(defn section-props->value-ratio
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) diagram-props
  ; {:total-value (integer)}
  ; @param (map) section-props
  ; {:value (integer)}
  ;
  ; @return (integer)
  [{:keys [total-value]} {:keys [value]}]
  (math/percent total-value value))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-section-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {data-background-color (keyword)}
  ;  :style (map)
  ;   {:width (string)}}
  [_ diagram-props {:keys [color] :as section-props}]
  (let [value-ratio (section-props->value-ratio diagram-props section-props)]
       {:data-fill-color color
        :style {:width (css/percent value-ratio)}}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [style] :as diagram-props}]
  (-> {:style style}
      (pretty-css/indent-attributes diagram-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;
  ; @return (map)
  [_ diagram-props]
  (-> {} (pretty-css/default-attributes diagram-props)
         (pretty-css/outdent-attributes diagram-props)))
