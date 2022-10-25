
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.interface.subs
    (:require [re-frame.api :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-interface
  ; @usage
  ;  (r get-interface db)
  ;
  ; @return (keyword)
  ;  :application-ui, :website-ui
  [db _]
  (get-in db [:ui :interface/meta-items :interface]))

(defn application-interface?
  ; @usage
  ;  (r application-interface? db)
  ;
  ; @return (boolean)
  [db _]
  (= :application-ui (r get-interface db)))

(defn website-interface?
  ; @usage
  ;  (r website-interface? db)
  ;
  ; @return (boolean)
  [db _]
  (= :website-ui (r get-interface db)))
