
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.body.views
    (:require [engines.item-handler.body.prototypes :as body.prototypes]
              [engines.item-handler.core.helpers    :as core.helpers]
              [re-frame.api                         :as r]
              [reagent.api                          :as reagent]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  [handler-id])

(defn body
  ; @param (keyword) handler-id
  ; @param (map) body-props
  ; {:auto-title? (boolean)(opt)
  ;   Default: false
  ;   W/ {:label-key ...}
  ;  :clear-behaviour (keyword)(opt)
  ;   :none, :on-leave, :on-item-change
  ;   Default: :none
  ;  :default-item (map)(opt)
  ;  :display-progress? (boolean)(opt)
  ;   Default: true
  ;  :initial-item (map)(opt)
  ;  :item-id (string)(opt)
  ;  :items-path (vector)(opt)
  ;   Default: core.helpers/default-items-path
  ;  :label-key (keyword)(opt)
  ;   W/ {:auto-title? true}
  ;  :query (vector)(opt)
  ;   XXX#7059 (source-code/cljs/engines/engine_handler/core/subs.cljs)
  ;  :suggestion-keys (keywords in vector)(opt)
  ;  :suggestions-path (vector)(opt)
  ;   Default: core.helpers/default-suggestions-path
  ;  :transfer-id (keyword)(opt)}
  ;
  ; @usage
  ; [body :my-handler {...}]
  [handler-id body-props]
  (let [body-props (body.prototypes/body-props-prototype handler-id body-props)]
       (reagent/lifecycles (core.helpers/component-id handler-id :body)
                           {:component-did-mount    (fn []  (r/dispatch [:item-handler/body-did-mount    handler-id body-props]))
                            :component-will-unmount (fn []  (r/dispatch [:item-handler/body-will-unmount handler-id]))
                            :component-did-update   (fn [%] (r/dispatch [:item-handler/body-did-update   handler-id %]))
                            :reagent-render         (fn []              [body-structure                  handler-id])})))
