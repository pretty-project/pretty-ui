
(ns pretty-website.multi-menu.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-body-attributes
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as menu-props}]
  (-> {:class :pw-multi-menu--body
       :style style}
      (pretty-build-kit/indent-attributes menu-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-attributes
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ menu-props]
  (-> {:class :pw-multi-menu}
      (pretty-build-kit/class-attributes   menu-props)
      (pretty-build-kit/state-attributes   menu-props)
      (pretty-build-kit/outdent-attributes menu-props)))
