
(ns pretty-elements.breadcrumbs.prototypes
    (:require [pretty-elements.properties.api :as pretty-elements.properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn crumb-props-prototype
  ; @ignore
  ;
  ; @param (integer) crumb-dex
  ; @param (map) crumb-props
  ;
  ; @return (map)
  [_ crumb-props]
  (-> crumb-props (pretty-elements.properties/default-font-props {:font-size :xs :font-weight :semi-bold})
                  (pretty-elements.properties/default-size-props {:max-width :s})
                  (pretty-elements.properties/default-text-props {:text-overflow :ellipsis})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn breadcrumbs-props-prototype
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;
  ; @return (map)
  [_ breadcrumbs-props]
  (-> breadcrumbs-props (pretty-elements.properties/default-flex-props {:gap :xs :orientation :horizontal :overflow :scroll})
                        (pretty-elements.properties/default-size-props {:height :content :width :content})))
