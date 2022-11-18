
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.download.effects
    (:require [engines.file-editor.body.subs           :as body.subs]
              [engines.file-editor.core.subs           :as core.subs]
              [engines.file-editor.download.events     :as download.events]
              [engines.file-editor.download.queries    :as download.queries]
              [engines.file-editor.download.validators :as download.validators]
              [re-frame.api                            :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :file-editor/request-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      (let [display-progress? (r body.subs/get-body-prop                             db editor-id :display-progress?)
            query             (r download.queries/get-request-content-query          db editor-id)
            validator-f      #(r download.validators/request-content-response-valid? db editor-id %)]
           {:db       (r download.events/request-content! db editor-id)
            :dispatch [:pathom/send-query! (r core.subs/get-request-id db editor-id)
                                           {:display-progress? display-progress?
                                            ; XXX#4057 (source-code/cljs/engines/item_handler/download/effects.cljs)
                                            :on-stalled [:file-editor/receive-content!  editor-id]
                                            :on-failure [:file-editor/set-engine-error! editor-id :failed-to-request-content]
                                            :query query :validator-f validator-f}]})))

(r/reg-event-fx :file-editor/receive-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id server-response]]
      ; Ha a [:file-editor/receive-content! ...] esemény megtörténésekor a body komponens már
      ; nincs a React-fába csatolva, akkor az esemény nem végez műveletet.
      (if (r body.subs/body-did-mount? db editor-id)
          {:db       (r download.events/receive-content! db editor-id server-response)
           :dispatch [:file-editor/content-received editor-id]})))

(r/reg-event-fx :file-editor/content-received
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]))
