
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popups.helpers
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (map)
  ;  {}
  [popup-id]
  (let [layout              @(a/subscribe [:ui/get-popup-prop popup-id :layout])
        horizontal-align    @(a/subscribe [:ui/get-popup-prop popup-id :horizontal-align])
        minimized?          @(a/subscribe [:ui/get-popup-prop popup-id :minimized?])
        min-width           @(a/subscribe [:ui/get-popup-prop popup-id :min-width])
        stretch-orientation @(a/subscribe [:ui/get-popup-prop popup-id :stretch-orientation])]
       (merge {:class                    :x-app-popups--element
               :data-animation           :reveal
               :data-horizontal-align    horizontal-align
               :data-layout              layout
               :data-minimized           (boolean minimized?)
               :data-min-width           min-width
               :data-nosnippet           true
               :data-stretch-orientation stretch-orientation
               :id                       (a/dom-value popup-id)
               :key                      (a/dom-value popup-id)})))

(defn popup-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (map)
  [popup-id]
  {})
