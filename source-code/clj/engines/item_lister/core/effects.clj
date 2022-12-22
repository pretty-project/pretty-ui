
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.core.effects
    (:require [engines.item-lister.core.events     :as core.events]
              [engines.item-lister.core.prototypes :as core.prototypes]
              [re-frame.api                        :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-lister/init-lister!
  ; @param (keyword) lister-id
  ; @param (map) lister-props
  ; {:collection-name (string)
  ;  :handler-key (keyword)
  ;  :item-namespace (keyword)}
  ;
  ; @usage
  ; [:item-lister/init-lister! :my-lister {...}]
  (fn [{:keys [db]} [_ lister-id lister-props]]
      (let [lister-props (core.prototypes/lister-props-prototype lister-id lister-props)]
           {:db       (r core.events/init-lister! db lister-id lister-props)
            :dispatch [:item-lister/reg-transfer-lister-props! lister-id lister-props]})))
