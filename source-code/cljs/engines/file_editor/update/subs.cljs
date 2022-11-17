
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.update.subs
    (:require [engines.engine-handler.update.subs :as update.subs]
              [engines.file-editor.body.subs      :as body.subs]
              [map.api                            :as map]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.update.subs
(def get-mutation-name   update.subs/get-mutation-name)
(def get-mutation-answer update.subs/get-mutation-answer)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-on-saved-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (metamorphic-event)
  [db [_ editor-id server-response]]
  ; XXX#6077 (source-code/cljs/engines/item_editor/update/subs.cljs)
  (if-let [on-saved (r body.subs/get-body-prop db editor-id :on-saved)]
          (let [saved-item (r get-mutation-answer db editor-id :save-item! server-response)]
               (r/metamorphic-event<-params on-saved (map/remove-namespace saved-item)))))
