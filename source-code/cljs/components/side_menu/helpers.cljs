
(ns components.side-menu.helpers
    (:require [pretty-css.api :as pretty-css]))

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
      (pretty-css/border-attributes menu-props)
      (pretty-css/color-attributes  menu-props)
      (pretty-css/indent-attributes menu-props)
      (pretty-css/style-attributes  menu-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-attributes
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  [_ menu-props]
  (-> {} (pretty-css/class-attributes   menu-props)
         (pretty-css/outdent-attributes menu-props)
         (pretty-css/state-attributes   menu-props)))
