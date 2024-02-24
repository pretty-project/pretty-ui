
(ns components.side-menu.helpers
    (:require [pretty-attributes.api        :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-inner-attributes
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:min-width (keyword, px or string)(opt)}
  ;
  ; @return (map)
  ; {:data-element-min-width (keyword, px or string)}
  [_ {:keys [min-width] :as menu-props}]
  (-> {:data-element-min-width min-width}
      (pretty-attributes/background-color-attributes menu-props)
      (pretty-attributes/border-attributes     menu-props)
      (pretty-attributes/inner-space-attributes menu-props)
      (pretty-attributes/style-attributes  menu-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-attributes
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  [_ menu-props]
  (-> {} (pretty-attributes/class-attributes  menu-props)
         (pretty-attributes/outer-space-attributes menu-props)
         (pretty-attributes/state-attributes  menu-props)))
