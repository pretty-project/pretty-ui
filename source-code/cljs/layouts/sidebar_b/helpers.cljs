
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
  ; The sidebar sensor has the same fill color as the sidebar body.
  ; The 'color-attributes' function only gets the fill-color property
  ; because, the sidebar might get a border-color value which is unwanted on
  ; the sensor!
  (pretty-css/color-attributes {} {:fill-color fill-color}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {:min-width (keyword)(opt)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-element-min-width (keyword)
  ;  :style (map)}
  [_ {:keys [min-width style] :as sidebar-props}]
  (-> {:data-element-min-width min-width
       :style                  style}
      (pretty-css/border-attributes sidebar-props)
      (pretty-css/color-attributes  sidebar-props)))

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
