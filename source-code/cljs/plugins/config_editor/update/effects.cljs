
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.config-editor.update.effects
    (:require [plugins.config-editor.backup.events     :as backup.events]
              [plugins.config-editor.core.subs         :as core.subs]
              [plugins.config-editor.update.queries    :as update.queries]
              [plugins.config-editor.update.validators :as update.validators]
              [x.app-core.api                          :as a :refer [r]]))



;; -- Save config effects -----------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :config-editor/save-config!
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [:config-editor/save-config! :my-editor]
  (fn [{:keys [db]} [_ editor-id]]
      (let [query        (r update.queries/get-save-config-query          db editor-id)
            validator-f #(r update.validators/save-config-response-valid? db editor-id %)]
           [:pathom/send-query! (r core.subs/get-request-id db editor-id)
                                {:display-progress? true
                                 :on-success [:config-editor/config-saved       editor-id]
                                 :on-failure [:config-editor/save-config-failed editor-id]
                                 :query query :validator-f validator-f}])))

(a/reg-event-fx
  :config-editor/config-saved
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ editor-id _]]
      {:db (r backup.events/backup-current-config! db editor-id)}))

(a/reg-event-fx
  :config-editor/save-config-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ editor-id _]]
      [:ui/render-bubble! {:body :failed-to-save}]))
