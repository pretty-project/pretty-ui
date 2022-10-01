
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.transfer.subs
    (:require [plugins.plugin-handler.body.subs :as body.subs]
              [re-frame.api                     :refer [r]]))



;; -- Transfer item subscriptions ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-transfer-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ plugin-id item-key]]
  ; XXX#8173
  ; A plugin body komponensének React-fába csatolódása előtt a body komponens
  ; transfer-id tulajdonsága még nem elérhető, ezért addig a plugin kliens-oldali
  ; azonosítója helyettesíti!
  ; Ha a plugin több példányban jelenik meg, példányonként eltérő azonosítóval,
  ; de csak egy szerver-oldali kezelő van inicializálva a plugin kiszolgálásához,
  ; akkor a body komponens React-fába csatólódása előtt nem lehetséges olyan funkciót
  ; megvalósítani, aminek szüksége van valamely szerver-oldali tulajdonságra!
  (if-let [transfer-id (r body.subs/get-body-prop db plugin-id :transfer-id)]
          (get-in db [:plugins :plugin-handler/transfer-items transfer-id item-key])
          (get-in db [:plugins :plugin-handler/transfer-items   plugin-id item-key])))
