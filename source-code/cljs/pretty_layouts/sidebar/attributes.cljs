
(ns pretty-layouts.sidebar.attributes
    (:require [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.layout.api     :as pretty-css.layout]))

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
      (pretty-css.appearance/background-attributes {:fill-color fill-color})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-body-attributes
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ sidebar-props]
  (-> {:class :pl-sidebar--body}
      (pretty-css.appearance/background-attributes        sidebar-props)
      (pretty-css.appearance/border-attributes       sidebar-props)
      (pretty-css.layout/element-size-attributes sidebar-props)
      (pretty-css.layout/indent-attributes       sidebar-props)
      (pretty-css.basic/style-attributes        sidebar-props)
      (pretty-css.appearance/theme-attributes        sidebar-props)))

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

   ; + outdent?
