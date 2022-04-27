
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.surface.helpers
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (map)
  ;  {}
  [surface-id]
  (let [horizontal-align @(a/subscribe [:ui/get-surface-prop surface-id :horizontal-align])
        trim-content?    @(a/subscribe [:ui/get-surface-prop surface-id :trim-content?])]
       {:class                 :x-app-surface--element
        :data-horizontal-align horizontal-align
        :data-trim-content     (boolean trim-content?)
        :id                    (a/dom-value surface-id)
        :key                   (a/dom-value surface-id)}))
