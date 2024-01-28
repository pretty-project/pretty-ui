
(ns components.side-menu.helpers
    (:require [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.layout.api     :as pretty-css.layout]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-body-attributes
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:min-width (keyword, px or string)(opt)}
  ;
  ; @return (map)
  ; {:data-element-min-width (keyword, px or string)}
  [_ {:keys [min-width] :as menu-props}]
  (-> {:data-element-min-width min-width}
      (pretty-css.appearance/background-attributes menu-props)
      (pretty-css.appearance/border-attributes     menu-props)
      (pretty-css.layout/indent-attributes menu-props)
      (pretty-css.basic/style-attributes  menu-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-attributes
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  [_ menu-props]
  (-> {} (pretty-css.basic/class-attributes   menu-props)
         (pretty-css.layout/outdent-attributes menu-props)
         (pretty-css.basic/state-attributes   menu-props)))
