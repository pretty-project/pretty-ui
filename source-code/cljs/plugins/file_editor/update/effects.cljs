
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.file-editor.update.effects
    (:require [plugins.file-editor.backup.events     :as backup.events]
              [plugins.file-editor.core.subs         :as core.subs]
              [plugins.file-editor.update.queries    :as update.queries]
              [plugins.file-editor.update.validators :as update.validators]
              [x.app-core.api                        :as a :refer [r]]))



;; -- Save content effects ----------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :file-editor/save-content!
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [:file-editor/save-content! :my-editor]
  (fn [{:keys [db]} [_ editor-id]]
      (let [query        (r update.queries/get-save-content-query          db editor-id)
            validator-f #(r update.validators/save-content-response-valid? db editor-id %)]
           [:pathom/send-query! (r core.subs/get-request-id db editor-id)
                                {:display-progress? true
                                 :on-success [:file-editor/content-saved       editor-id]
                                 :on-failure [:file-editor/save-content-failed editor-id]
                                 :query query :validator-f validator-f}])))

(a/reg-event-fx
  :file-editor/content-saved
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ editor-id _]]
      ; A file-editor plugin az item-editor pluginnal ellentétben a tartalom
      ; mentésének befejeződésekor nem lép ki a szerkesztőből, ezért szükséges
      ; a tartalomról tárolt másolatot frissíteni, hogy a backup.subs/form-changed?
      ; és a backup.subs/content-changed? függvények kimentei visszaálljanak alaphelyzetbe.
      {:db       (r backup.events/backup-current-content! db editor-id)
       :dispatch [:ui/render-bubble! ::content-saved-notification
                                     {:body :saved}]}))

(a/reg-event-fx
  :file-editor/save-content-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ editor-id _]]
      [:ui/render-bubble! {:body :failed-to-save}]))
