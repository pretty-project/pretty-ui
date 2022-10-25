
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.engine-handler.transfer.subs
    (:require [plugins.engine-handler.body.subs :as body.subs]
              [re-frame.api                     :refer [r]]))



;; -- Transfer item subscriptions ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-transfer-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ engine-id item-key]]
  ; XXX#8173
  ; Az engine body komponensének React-fába csatolódása előtt a body komponens
  ; transfer-id tulajdonsága még nem elérhető, ezért addig az engine kliens-oldali
  ; azonosítója helyettesíti!
  ; Ha az engine több példányban jelenik meg, példányonként eltérő azonosítóval,
  ; de csak egy szerver-oldali kezelő van inicializálva az engine kiszolgálásához,
  ; akkor a body komponens React-fába csatólódása előtt nem lehetséges olyan funkciót
  ; megvalósítani, aminek szüksége van valamely szerver-oldali tulajdonságra!
  (if-let [transfer-id (r body.subs/get-body-prop db engine-id :transfer-id)]
          (get-in db [:engines :engine-handler/transfer-items transfer-id item-key])
          (get-in db [:engines :engine-handler/transfer-items   engine-id item-key])))
