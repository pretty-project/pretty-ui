
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.user.install-handler.side-effects
    (:require [x.core.api                       :as x.core]
              [x.user.user-handler.side-effects :as user-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn installer
  ; @return (boolean)
  []
  (boolean (user-handler.side-effects/add-user! {:email-address "root@monotech.hu"
                                                 :password      "Monotech.420"
                                                 :first-name    "Tech"
                                                 :last-name     "Mono"
                                                 :pin           0000
                                                 :roles         ["root"]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-installer! ::installer installer)
