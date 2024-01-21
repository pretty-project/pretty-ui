
(ns components.side-menu.helpers
    (:require [pretty-build-kit.api :as pretty-build-kit]))

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
      (pretty-build-kit/border-attributes menu-props)
      (pretty-build-kit/color-attributes  menu-props)
      (pretty-build-kit/indent-attributes menu-props)
      (pretty-build-kit/style-attributes  menu-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-attributes
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  [_ menu-props]
  (-> {} (pretty-build-kit/class-attributes   menu-props)
         (pretty-build-kit/outdent-attributes menu-props)
         (pretty-build-kit/state-attributes   menu-props)))
