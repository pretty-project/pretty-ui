
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.dictionary.books.link)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:automatic-link
           {:en "Automatic link"
            :hu "Automatikus hivatkozás"}
           :copy-link!
           {:en "Copy link"
            :hu "Hivatkozás másolása"}
           :link
           {:en "Link"
            :hu "Hivatkozás"}
           :links
           {:en "Links"
            :hu "Hivatkozások"}
           :link-label
           {:en "Link label"
            :hu "Hivatkozás címke"}
           :public-link
           {:en "Public link"
            :hu "Nyilvános hivatkozás"}
           :open-link!
           {:en "Open link"
            :hu "Hivatkozás megnyitása"}
           :open-link-in-new-page!
           {:en "Open in new page"
            :hu "Megnyitás új oldalon"}
           :open-link-in-new-tab!
           {:en "Open in new tab"
            :hu "Megnyitás új lapon"}
           :the-link-you-followed-may-be-broken
           ; Always place a comma before or when it begins an independent clause!
           {:en "The link you followed may be broken, or the page may have been removed"
            :hu "Előfordulhat, hogy a megadott hivatkozás nem megfelelő, vagy az oldalt áthelyeztük"}
           :the-link-has-been-broken
           ; Always place a comma before or when it begins an independent clause!
           {:en "The link has been broken, or the page have been removed"
            :hu "A megadott hivatkozás nem megfelelő, vagy az oldalt áthelyeztük"}})
