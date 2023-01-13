
(ns layouts.sidebar-b.helpers
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-sensor-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {:fill-color (keyword or string)(opt)}
  ;
  ; @return (map)
  [_ {:keys [fill-color]}]
  (pretty-css/apply-color {} :fill-color :data-fill-color fill-color))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {:border-radius (keyword)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :min-width (keyword)(opt)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-border-radius (keyword)
  ;  :data-element-min-width (keyword)
  ;  :style (map)}
  [_ {:keys [border-radius fill-color min-width style]}]
  (-> {:data-border-radius     border-radius
       :data-element-min-width min-width
       :style                  style}
      (pretty-css/apply-color :fill-color :data-fill-color fill-color)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {:position (keyword)}
  ;
  ; @return (map)
  ; {:data-position-fixed (map)}
  [_ {:keys [position]}]
  {:data-position-fixed (case position :left :tl :tr)})
