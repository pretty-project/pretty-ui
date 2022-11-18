
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.update.effects
    (:require [engines.file-editor.backup.events     :as backup.events]
              [engines.file-editor.body.subs         :as body.subs]
              [engines.file-editor.core.subs         :as core.subs]
              [engines.file-editor.update.queries    :as update.queries]
              [engines.file-editor.update.subs       :as update.subs]
              [engines.file-editor.update.validators :as update.validators]
              [re-frame.api                          :as r :refer [r]]))



;; -- Save content effects ----------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :file-editor/save-content!
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [:file-editor/save-content! :my-editor]
  (fn [{:keys [db]} [_ editor-id]]
      (let [display-progress? (r body.subs/get-body-prop                        db editor-id :display-progress?)
            query             (r update.queries/get-save-content-query          db editor-id)
            validator-f      #(r update.validators/save-content-response-valid? db editor-id %)]
           [:pathom/send-query! (r core.subs/get-request-id db editor-id)
                                {:display-progress? display-progress?
                                 :on-success [:file-editor/content-saved       editor-id]
                                 :on-failure [:file-editor/save-content-failed editor-id]
                                 :query query :validator-f validator-f}])))

(r/reg-event-fx :file-editor/content-saved
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ editor-id server-response]]
      ; A file-editor engine a tartalom mentésének befejeződésekor ...
      ; ... az item-editor engine-nel ellentétben nem lép ki a szerkesztőből,
      ;     ezért szükséges a tartalomról tárolt másolatot frissíteni,
      ;     hogy a backup.subs/form-changed? és a backup.subs/content-changed?
      ;     függvények kimentei visszaálljanak alaphelyzetbe.
      ; ... megtörténik a body komponens számára esetlegesen átadott on-saved esemény.
      {:db          (r backup.events/backup-current-content! db editor-id)
       :dispatch-n [(r update.subs/get-on-saved-event db editor-id server-response)
                    [:x.ui/render-bubble! ::content-saved-dialog {:body :saved}]]}))

(r/reg-event-fx :file-editor/save-content-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ editor-id _]]
      [:x.ui/render-bubble! ::save-content-failed-notification {:body :failed-to-save}]))
