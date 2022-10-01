
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.file-editor.download.effects
    (:require [plugins.file-editor.body.subs           :as body.subs]
              [plugins.file-editor.core.subs           :as core.subs]
              [plugins.file-editor.download.events     :as download.events]
              [plugins.file-editor.download.queries    :as download.queries]
              [plugins.file-editor.download.validators :as download.validators]
              [re-frame.api                            :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx
  :file-editor/request-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      (let [query        (r download.queries/get-request-content-query          db editor-id)
            validator-f #(r download.validators/request-content-response-valid? db editor-id %)]
           {:db       (r download.events/request-content! db editor-id)
            :dispatch [:pathom/send-query! (r core.subs/get-request-id db editor-id)
                                           {:display-progress? true
                                            ; XXX#4057
                                            ; Az on-stalled időzítéssel a UI változásai egyszerre történnek
                                            ; meg a lekérés okozta {:editor-disabled? true} állapot megszűnésével
                                            :on-stalled [:file-editor/receive-content! editor-id]
                                            :on-failure [:file-editor/set-error-mode!  editor-id]
                                            :query query :validator-f validator-f}]})))

(r/reg-event-fx
  :file-editor/receive-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id server-response]]
      ; Ha a [:file-editor/receive-content! ...] esemény megtörténésekor a body komponens már
      ; nincs a React-fába csatolva, akkor az esemény nem végez műveletet.
      (if (r body.subs/body-did-mount? db editor-id)
          {:db       (r download.events/receive-content! db editor-id server-response)
           :dispatch [:file-editor/content-received editor-id]})))

(r/reg-event-fx
  :file-editor/content-received
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]))
