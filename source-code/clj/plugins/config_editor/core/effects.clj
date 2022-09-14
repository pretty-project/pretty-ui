
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.config-editor.core.effects
    (:require [plugins.config-editor.core.events     :as core.events]
              [plugins.config-editor.core.prototypes :as core.prototypes]
              [x.server-core.api                     :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :config-editor/init-editor!
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:base-route (string)(opt)
  ;   :handler-key (keyword)
  ;   :on-route (metamorphic-event)(opt)
  ;   :route-title (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [:config-editor/init-editor! :my-editor {...}]
  (fn [{:keys [db]} [_ editor-id {:keys [base-route] :as editor-props}]]
      (let [editor-props (core.prototypes/editor-props-prototype editor-id editor-props)]
           {:db         (r core.events/init-editor! db editor-id editor-props)
            :dispatch-n [[:config-editor/reg-transfer-editor-props! editor-id editor-props]
                         (if base-route [:config-editor/add-base-route! editor-id editor-props])]})))
