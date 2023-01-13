
(ns elements.blank.helpers
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn blank-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [style] :as blank-props}]
  (merge (pretty-css/indent-attributes blank-props)
         {:style style}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn blank-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ;
  ; @return (map)
  [_ blank-props]
  (merge (pretty-css/default-attributes blank-props)
         (pretty-css/outdent-attributes blank-props)))
