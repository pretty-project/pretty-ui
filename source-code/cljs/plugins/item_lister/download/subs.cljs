
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.download.subs
    (:require [plugins.plugin-handler.core.subs     :as core.subs]
              [plugins.plugin-handler.download.subs :as download.subs]
              [x.app-core.api                       :as a :refer [r]]))



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
  ; TEMP#4681 (Megfelelőbb nevet kell neki találni!)
  ; A listaelemek újratöltésekor a letöltött elemek törlődnek és a plugin {:data-received? false}
  ; állapotba lép, ezért a data-received? feliratkozás nem mindenhol használható.
  ; Az elemek első letöltődése után a plugin {:first-data-received? true} állapotba lép,
  ; és nem a listaelemek újratöltésekor és más esetben sem lép ki ebből az állapotból.
  (r core.subs/get-meta-item db lister-id :first-data-received?))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/data-received? :my-lister]
(a/reg-sub :item-lister/data-received? data-received?)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/first-data-received? :my-lister]
(a/reg-sub :item-lister/first-data-received? first-data-received?)
