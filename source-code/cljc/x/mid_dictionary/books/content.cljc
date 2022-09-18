
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.books.content)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:content
           {:en "Content"
            :hu "Tartalom"}
           :contents
           {:en "Contents"
            :hu "Tartalmak"}
           :content-not-found
           {:en "Content not found"
            :hu "A tartalom nem található"}
           :content-visibility
           {:en "Content visibility"
            :hu "Tartalom láthatósága"}
           :only-visible-to-editors
           {:en "Only visible to editors"
            :hu "Csak a szerkesztők számára látható"}
           :private-content
           {:en "Private content"
            :hu "Privát tartalom"}
           :public-content
           {:en "Public content"
            :hu "Publikus tartalom"}
           :select-content!
           {:en "Select content"
            :hu "Tartalom kiválasztása"}
           :select-contents!
           {:en "Select contents"
            :hu "Tartalmak kiválasztása"}
           :visible-to-everyone
           {:en "Visible to everyone"
            :hu "Mindenki számára látható"}})
