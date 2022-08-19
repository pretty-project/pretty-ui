

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.touch-handler.subs)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn touch-detected?
  ; @usage
  ;  (r environment/touch-detected? db)
  ;
  ; @return (boolean)
  [db _]
  (get-in db [:environment :touch-handler/meta-items :touch-detected?]))
