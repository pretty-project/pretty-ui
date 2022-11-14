
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.touch-handler.subs)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn touch-detected?
  ; @usage
  ;  (r touch-detected? db)
  ;
  ; @return (boolean)
  [db _]
  (get-in db [:x.environment :touch-handler/meta-items :touch-detected?]))
