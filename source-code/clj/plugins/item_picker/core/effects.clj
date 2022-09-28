
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-picker.core.effects
    (:require [plugins.item-picker.core.events     :as core.events]
              [plugins.item-picker.core.prototypes :as core.prototypes]
              [x.server-core.api                   :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-picker/init-picker!
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:handler-key (keyword)
  ;   :item-namespace (keyword)}
  ;
  ; @usage
  ;  [:item-picker/init-picker! :my-picker {...}]
  (fn [{:keys [db]} [_ picker-id picker-props]]
      (let [picker-props (core.prototypes/picker-props-prototype picker-id picker-props)]
           {:db       (r core.events/init-picker! db picker-id picker-props)
            :dispatch [:item-picker/reg-transfer-picker-props! picker-id picker-props]})))
