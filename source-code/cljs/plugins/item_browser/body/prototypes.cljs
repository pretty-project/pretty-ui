
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.body.prototypes
    (:require [plugins.item-browser.core.helpers   :as core.helpers]
              [plugins.item-lister.body.prototypes :as body.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) body-props
  ;
  ; @return (map)
  ;  {:item-path (vector)}
  [browser-id body-props]
  ; XXX#6177
  (merge {:item-path (core.helpers/default-item-path browser-id)}
         (body.prototypes/body-props-prototype browser-id body-props)))
