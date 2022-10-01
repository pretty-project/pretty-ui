
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.download.subs
    (:require [plugins.plugin-handler.core.subs     :as core.subs]
              [plugins.plugin-handler.download.subs :as download.subs]
              [re-frame.api                         :as r :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.download.subs
(def get-resolver-id download.subs/get-resolver-id)
(def data-received?  download.subs/data-received?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn first-data-received?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  ; TEMP#4681 Megfelelőbb nevet kell neki találni!
  ;
  ; A listaelemek újratöltésekor a letöltött elemek törlődnek és a plugin kilép
  ; a {:data-received? true} állapotból, ezért a data-received? feliratkozás
  ; nem mindenre használható.
  ; Az elemek első letöltődése után a plugin {:first-data-received? true}
  ; állapotba lép és abban is marad, mert a listaelemek újratöltésekor és más
  ; esetben sem lép ki ebből az állapotból.
  (r core.subs/get-meta-item db lister-id :first-data-received?))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/data-received? :my-lister]
(r/reg-sub :item-lister/data-received? data-received?)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/first-data-received? :my-lister]
(r/reg-sub :item-lister/first-data-received? first-data-received?)
