
(ns components.side-menu.helpers
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-body-attributes
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:min-width (keyword)(opt)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-element-min-width (keyword)
  ;  :style (map)}
  [_ {:keys [min-width style] :as menu-props}]
  (-> {:data-element-min-width min-width
       :style                  style}
      (pretty-css/indent-attributes menu-props)))

(defn menu-attributes
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:position (keyword)}
  ;
  ; @return (map)
  ; {:data-position (keyword)}
  [_ {:keys [position] :as menu-props}]
  (-> {:data-position position}
      (pretty-css/default-attributes menu-props)
      (pretty-css/outdent-attributes menu-props)))
