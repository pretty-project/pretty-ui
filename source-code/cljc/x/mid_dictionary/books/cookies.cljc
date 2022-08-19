
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.books.cookies)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:analytics-cookies
           {:en "Analytics cookies"
            :hu "Statisztikai sütik"}
           :cookie-settings
           {:en "Cookie settings"
            :hu "Süti beállítások"}
           :necessary-cookies
           {:en "Necessary cookies"
            :hu "Szükséges sütik"}
           :remove-stored-cookies!
           {:en "Remove stored cookies"
            :hu "Tárolt sütik eltávolítása"}
           :remove-stored-cookies?
           {:en "Are you sure you want to remove stored cookies?"
            :hu "Biztos vagy benne, hogy szeretnéd eltávolítani a tárolt sütiket?"}
           :this-website-uses-cookies
           {:en "This website uses cookies to analyze traffic and ensure you get the best experience"
            :hu "Ez a webhely sütiket használ statisztikai adatok gyűjtésére és a jobb felhasználói élmény biztosítására"}
           :use-cookies
           {:en "Use cookies"
            :hu "Sütik használata"}
           :user-experience-cookies
           {:en "User-experience cookies"
            :hu "Felhasználói élmény sütik"}})
