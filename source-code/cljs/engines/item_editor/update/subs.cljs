
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-editor.update.subs
    (:require [engines.engine-handler.update.subs :as update.subs]
              [engines.item-editor.body.subs      :as body.subs]
              [engines.item-editor.core.subs      :as core.subs]
              [engines.item-editor.transfer.subs  :as transfer.subs]
              [keyword.api                        :as keyword]
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
  ; XXX#6077
  (if-let [on-saved (r body.subs/get-body-prop db editor-id :on-saved)]
          (let [new-item?  (r core.subs/new-item? db editor-id)
                saved-item (r get-mutation-answer db editor-id (if new-item? :add-item! :save-item!) server-response)]
               (r/metamorphic-event<-params on-saved (map/remove-namespace saved-item)))))



;; -- Save item subscriptions -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-saved-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @example
  ;  (r get-saved-item-id :my-editor {my-handler/save-item! {:my-type/id "my-item"}})
  ;  =>
  ;  "my-item"
  ;
  ; @return (string)
  [db [_ editor-id server-response]]
  (let [new-item?      (r core.subs/new-item? db editor-id)
        saved-item     (r get-mutation-answer db editor-id (if new-item? :add-item! :save-item!) server-response)
        item-namespace (r transfer.subs/get-transfer-item db editor-id :item-namespace)
        id-key         (keyword/add-namespace item-namespace :id)]
       (id-key saved-item)))
