
(ns pretty-layouts.sidebar.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-sensor-attributes
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {:fill-color (keyword or string)(opt)}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [fill-color]}]
  ; The sidebar sensor has the same fill color as the sidebar body.
  ; The 'color-attributes' function only gets the fill-color property
  ; because, the sidebar might get a border-color value which is unwanted on
  ; the sensor!
  (-> {:class :pl-sidebar--sensor}
      (pretty-css/color-attributes {:fill-color fill-color})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-body-attributes
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as sidebar-props}]
  (-> {:class :pl-sidebar--body
       :style style}
      (pretty-css/border-attributes           sidebar-props)
      (pretty-css/color-attributes            sidebar-props)
      (pretty-css/element-min-size-attributes sidebar-props)
      (pretty-css/indent-attributes           sidebar-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-attributes
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {:position (keyword)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-position-fixed (map)}
  [_ {:keys [position]}]
  {:class               :pl-sidebar
   :data-position-fixed (case position :left :tl :tr)})