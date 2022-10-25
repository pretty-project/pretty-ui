
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.core.effects
    (:require [engines.file-editor.core.events     :as core.events]
              [engines.file-editor.core.prototypes :as core.prototypes]
              [re-frame.api                        :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :file-editor/init-editor!
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:base-route (string)(opt)
  ;   :handler-key (keyword)
  ;   :on-route (metamorphic-event)(opt)
  ;   :route-title (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [:file-editor/init-editor! :my-editor {...}]
  (fn [{:keys [db]} [_ editor-id {:keys [base-route] :as editor-props}]]
      (let [editor-props (core.prototypes/editor-props-prototype editor-id editor-props)]
           {:db         (r core.events/init-editor! db editor-id editor-props)
            :dispatch-n [[:file-editor/reg-transfer-editor-props! editor-id editor-props]
                         (if base-route [:file-editor/add-base-route! editor-id editor-props])]})))
