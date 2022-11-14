
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.scroll-handler.subs)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn scrolled-to-top?
  ; @usage
  ;  (r scrolled-to-top? db)
  ;
  ; @return (boolean)
  [db _]
  (let [scrolled-to-top? (get-in db [:x.environment :sroll-handler/meta-items :scrolled-to-top?])]
       (boolean scrolled-to-top?)))
